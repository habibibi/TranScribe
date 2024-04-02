package com.ikp.transcribe.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.ikp.transcribe.MainViewModel
import com.ikp.transcribe.R
import com.ikp.transcribe.databinding.FragmentChartBinding
import java.text.DecimalFormat

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
    private val viewModel : MainViewModel by activityViewModels()

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
        // Inflate the layout for this fragment
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val total = viewModel.getTotal()
        if (total == 0.0) {
            binding.pieChart.visibility = View.GONE
            binding.pemasukan.text = getString(R.string.pemasukan_kosong)
            binding.pengeluaran.text = getString(R.string.pengeluaran_kosong)
        } else {
            val income = viewModel.getIncome()
            val expense = viewModel.getExpense()

            val pieEntries = ArrayList<PieEntry>()
            val label = ""
            val typeAmountMap: MutableMap<String, Double> = HashMap()
            typeAmountMap["Pemasukan"] = income
            typeAmountMap["Pengeluaran"] = expense
            for (type in typeAmountMap.keys) {
                pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
            }

            val colors = ArrayList<Int>()
            colors.add(ContextCompat.getColor(requireContext(), R.color.pemasukan))
            colors.add(ContextCompat.getColor(requireContext(), R.color.pengeluaran))

            val pieDataSet = PieDataSet(pieEntries, label)
            pieDataSet.valueTextSize = 12f
            pieDataSet.colors = colors
            pieDataSet.sliceSpace = 3f
            pieDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)

            val pieData = PieData(pieDataSet)
            pieData.setDrawValues(true)

            binding.apply {
                pieChart.setDrawEntryLabels(false)
                pieChart.setUsePercentValues(true)
                pieData.setValueFormatter(PercentFormatter(pieChart))

                pieChart.description.isEnabled = false
                pieChart.centerText = "Data Transaksi"
                pieChart.setCenterTextSize(12f)
                pieChart.holeRadius = 40f
                pieChart.transparentCircleRadius = 45f
                pieChart.setData(pieData)
                pieChart.invalidate()

                val formattedIncome = DecimalFormat("#,###.##").format(income)
                val formattedExpense = DecimalFormat("#,###.##").format(expense)
                pemasukan.text = getString(R.string.pemasukan_chart, formattedIncome)
                pengeluaran.text = getString(R.string.pengeluaran_chart, formattedExpense)
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
