package com.ikp.transcribe.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikp.transcribe.MainViewModel
import com.ikp.transcribe.databinding.FragmentScanConfirmationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


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
    private var hasFetch : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasFetch = savedInstanceState?.getBoolean("hasFetch") ?: false
        if (!hasFetch) {
            val imageFile = File(requireContext().cacheDir, "tmp.jpeg")
            lifecycleScope.launch(Dispatchers.IO){
                viewModel.fetchBillItems(imageFile)
                hasFetch = true
                view?.post{
                    finishLoading()
                }
            }
        }
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
        rv = binding.billItemList
        rv.layoutManager = LinearLayoutManager(activity)
        val divider =  DividerItemDecoration(rv.context, LinearLayout.VERTICAL)
        rv.addItemDecoration(divider)

        if (hasFetch) finishLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private fun finishLoading(){
        rv.adapter = ItemListAdapter(viewModel.getBillItems())
        binding.loadingPanel.visibility = View.GONE
        binding.scanConfirmationMenu.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("hasFetch",hasFetch)
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