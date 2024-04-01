package com.ikp.transcribe.ui.scan

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.ikp.transcribe.MainViewModel
import com.ikp.transcribe.R
import com.ikp.transcribe.databinding.FragmentScanConfirmationBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScanConfirmationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanConfirmationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentScanConfirmationBinding? = null
    private val binding get() = _binding!!
    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var rv : RecyclerView
    private lateinit var fused : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fused = LocationServices.getFusedLocationProviderClient(requireContext())
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted || ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    updateLocation(fused.lastLocation)
                }
            }

        if (isPermissionsGranted()){
            updateLocation(fused.lastLocation)
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }


        val billItems = viewModel.getBillItems()
        rv = binding.billItemList
        rv.adapter = ItemListAdapter(billItems)
        val divider =  DividerItemDecoration(rv.context, LinearLayout.VERTICAL)
        rv.addItemDecoration(divider)
        rv.layoutManager = LinearLayoutManager(activity)

        binding.categoryValue.setText(getString(R.string.pengeluaran))

        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)
        binding.dateValue.setText(formattedDate)

        var totalPrice  = 0.0
        for (item in billItems){
            totalPrice += item.quantity * item.price
        }
        binding.totalPriceValue.setText(requireContext().getString(R.string.nominal,totalPrice))

        binding.locationTextView.setEndIconOnClickListener {
            if (isPermissionsGranted()){
                updateLocation(fused.lastLocation)
            } else {
                Toast.makeText(requireContext(),"Location access permission required",Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }

        binding.saveButton.setOnClickListener {
            var error = false

            if (binding.transactionNameInput.text.toString().isBlank()){
                error = true
                binding.transactionNameTextView.error = "Nama transaksi tidak boleh kosong."
            } else {
                binding.transactionNameTextView.isErrorEnabled = false
                binding.transactionNameTextView.error = null

            }

            if (binding.locationValue.text.toString().isBlank()){
                error = true
                binding.locationTextView.error = "Lokasi tidak boleh kosong."
            } else {
                binding.locationTextView.isErrorEnabled = false
                binding.locationTextView.error = null
            }

            if (!error) {
                viewModel.addTransaction(
                    viewModel.getEmail(),
                    binding.transactionNameInput.text.toString(),
                    "Pengeluaran",
                    totalPrice,
                    binding.locationValue.text.toString(),
                    formattedDate,
                )
                Navigation.findNavController(requireView()).navigate(R.id.action_scanConfirmationFragment_to_navigation_transaction)
            }
        }
    }

    private fun isPermissionsGranted() : Boolean{
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun updateLocation(task : Task<Location>){
        task.addOnSuccessListener{
            if(it!=null){
                val lokasi = binding.locationValue
                val ambiladdress = Geocoder(requireContext(), Locale.getDefault())
                val ambilsatu = ambiladdress.getFromLocation(it.latitude,it.longitude,1)
                val alamatasli = ambilsatu?.get(0)?.getAddressLine(0)
                lokasi.setText(alamatasli)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScanConfirmationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanConfirmationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}