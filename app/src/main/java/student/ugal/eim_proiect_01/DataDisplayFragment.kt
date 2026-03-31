package student.ugal.eim_proiect_01

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DataDisplayFragment : Fragment() {

    companion object {
        private const val ARG_KEY = "arg_key"
        private const val ARG_VALUE = "arg_value"

        fun newInstance(key: String, value: Float): DataDisplayFragment {
            val fragment = DataDisplayFragment()
            val args = Bundle()
            args.putString(ARG_KEY, key)
            args.putFloat(ARG_VALUE, value)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data_display, container, false)
        val textViewKey = view.findViewById<TextView>(R.id.textViewKey)
        val textViewValue = view.findViewById<TextView>(R.id.textViewValue)

        val key = arguments?.getString(ARG_KEY) ?: "N/A"
        val value = arguments?.getFloat(ARG_VALUE) ?: 0.0f

        textViewKey.text = "Cheie: $key"
        textViewValue.text = "Valoare: $value"

        return view
    }
}
