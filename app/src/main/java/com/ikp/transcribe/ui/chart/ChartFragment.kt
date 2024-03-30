package com.ikp.transcribe.ui.chart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ikp.transcribe.R
import com.ikp.transcribe.data.AppDatabase
import com.ikp.transcribe.databinding.FragmentChartBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val dao = AppDatabase.getInstance(requireContext()).TransactionDao()
        val transactions = email?.let { dao.getTransaction(it) }

        var totalIncome = 0
        var totalExpense = 0
        var totalTransaction = 0
        if (transactions != null) {
            for (transaction in transactions) {
                if (transaction.kategori == "Pemasukan") {
                    totalIncome += transaction.nominal ?: 0
                } else if (transaction.kategori == "Pengeluaran") {
                    totalExpense += transaction.nominal ?: 0
                }
                totalTransaction += transaction.nominal ?: 0
            }
        }

        if (totalTransaction == 0) {
            binding.donutChart.visibility = View.GONE
            binding.pemasukan.text = getString(R.string.pemasukan_kosong)
            binding.pengeluaran.text = getString(R.string.pengeluaran_kosong)
        } else {
            val colorPemasukan = ContextCompat.getColor(requireContext(), R.color.pemasukan)
            val colorPengeluaran = ContextCompat.getColor(requireContext(), R.color.pengeluaran)
            val animationDuration = 1000L
            val valuePemasukan = totalIncome.toFloat() / totalTransaction.toFloat() * 360f
            val valuePengeluaran = totalExpense.toFloat() / totalTransaction.toFloat() * 360f
            val donutSet = listOf(
                valuePemasukan,
                valuePengeluaran
            )

            binding.apply {
                donutChart.donutColors = intArrayOf(
                    colorPemasukan,
                    colorPengeluaran
                )
                donutChart.animation.duration = animationDuration
                donutChart.animate(donutSet)
                pemasukan.text = getString(R.string.pemasukan, totalIncome)
                pengeluaran.text = getString(R.string.pengeluaran, totalExpense)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}