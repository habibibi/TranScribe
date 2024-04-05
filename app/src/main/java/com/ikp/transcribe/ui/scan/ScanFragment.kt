package com.ikp.transcribe.ui.scan

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.ikp.transcribe.MainActivity
import com.ikp.transcribe.MainViewModel
import com.ikp.transcribe.R
import com.ikp.transcribe.databinding.FragmentScanBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentScanBinding? = null
    private val viewModel : MainViewModel by activityViewModels()
    private var isCameraStarted = true
    private lateinit var cameraController : LifecycleCameraController
    private lateinit var previewView: PreviewView

    private val binding get() = _binding!!

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        result  ->
            if (result != null){
                val uriResult : Uri = result
                val inputstream = requireContext().contentResolver.openInputStream(uriResult)
                val target = File(requireContext().cacheDir, "tmp.jpeg")
                if(target.exists().not()) {
                    target.createNewFile()
                }
                inputstream!!.copyTo(target.outputStream())
                requireView().post {
                    uploadImage()
                }
            }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                hideCamera()
                binding.cameraTextView.text = getString(R.string.camera_permission_required)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previewView = binding.scannerCameraView
        hideCamera()

        binding.captureButton.setOnClickListener {
            capture()
        }
        binding.openImageButton.setOnClickListener{
            chooseImage()
        }

        val activity = requireActivity() as MainActivity
        val isConnected = activity.getNetworkStatusLiveData().value == true
        if (isConnected){
            onActiveNetwork()
        } else {
            onLostNetwork()
        }

        activity.getNetworkStatusLiveData().observe(viewLifecycleOwner, object : Observer<Boolean>{
            override fun onChanged(value: Boolean) {
                if (value){
                    onActiveNetwork()
                } else {
                    onLostNetwork()
                }
            }
        })
    }

    private fun onActiveNetwork(){
        binding.openImageButton.isEnabled = true
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun onLostNetwork(){
        stopCamera()
        binding.cameraTextView.text = getString(R.string.no_connection)
        binding.openImageButton.isEnabled = false
    }

    private fun startCamera(){
        cameraController = LifecycleCameraController(requireContext())
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        previewView.controller = cameraController
        previewView.visibility = View.VISIBLE
        isCameraStarted = true
        binding.captureButton.isEnabled = true
    }

    private fun capture(){
        val cacheDir = requireContext().cacheDir
        val outputFileOptions = OutputFileOptions.Builder(File(cacheDir, "tmp.jpeg")).build()
        cameraController.takePicture(outputFileOptions, Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException)
                {
                    Log.e("camera", error.toString())
                }

                override fun onCaptureStarted() {
                    super.onCaptureStarted()
                    requireView().post{

                        binding.capturedPreview.setImageBitmap(previewView.bitmap)
                        binding.capturedPreview.visibility = View.VISIBLE
                    }
                }
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    requireView().post {
                        uploadImage()
                    }
                }
        } )
    }

    private fun uploadImage(){
        binding.loadingPanel.visibility = View.VISIBLE
        val imageFile = File(requireContext().cacheDir, "tmp.jpeg")
        lifecycleScope.launch(Dispatchers.IO){
            val ret = viewModel.fetchBillItems(imageFile)
            if (ret == null) {
                view?.post {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_scan_to_scanConfirmationFragment)
                }
            } else {
                view?.post{
                    binding.loadingPanel.visibility = View.GONE
                    binding.capturedPreview.visibility = View.GONE
                    Toast.makeText(requireContext(),ret.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun chooseImage(){
        imagePickerLauncher.launch("image/*")
    }

    private fun hideCamera(){
        previewView.visibility = View.GONE
        binding.captureButton.isEnabled = false
    }

    private fun stopCamera(){
        if (!this::cameraController.isInitialized || !isCameraStarted) return
        cameraController.unbind()
        isCameraStarted = false
        hideCamera()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}