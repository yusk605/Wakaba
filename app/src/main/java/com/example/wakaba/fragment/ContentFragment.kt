
package com.example.wakaba.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.wakaba.R
import com.example.wakaba.model.WakabaViewModel
import com.example.wakaba.room.Wakaba
import kotlinx.android.synthetic.main.wakaba_item.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ContentFragment : Fragment() {

    private val viewModel:WakabaViewModel by lazy{
        ViewModelProvider(this).get(WakabaViewModel::class.java)
    }
    private var txtTimeStamp:TextView?=null
    private var editTitle:EditText?=null
    private var editDatails:EditText?=null
    private var argWakaba:Wakaba?=null
    private var contentDate:TextView?=null
    private var contentRg:RadioGroup?=null
    private var contentImgDateBtn:ImageView?=null
    private var changeBtn: Button?=null
    private var wakabaNo:Long?=null
    val COMPLETE:Int=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            arguments?.let {arg-> argWakaba=ContentFragmentArgs.fromBundle(arg).bean }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_content,container,false)
        argWakaba?.let { init(view, it) }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changeBtn?.setOnClickListener {
            Log.d("vm","RESULT_OK")
            wakabaNo?.let { p -> setChanges(viewModel, p) }
            Navigation.findNavController(it).navigate(
                ContentFragmentDirections
                    .actionContentFragmentToWakabaListFragment(COMPLETE))
        }
        activity?.let {context->
            contentImgDateBtn?.setOnClickListener {
                showDatePickerDialog(context)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun init(view:View,wakaba: Wakaba) {
        view.run {
            txtTimeStamp        = findViewById(R.id.content_stamp)
            editTitle           = findViewById(R.id.content_title)
            editDatails         = findViewById(R.id.content_details)
            contentDate         = findViewById(R.id.content_date)
            contentImgDateBtn   = findViewById(R.id.content_img_date)
            contentRg           = findViewById(R.id.content_rg)
            changeBtn           = findViewById(R.id.content_change)
        }
        txtTimeStamp?.text =wakaba.timeStamp
        editTitle?.setText(wakaba.wakabaTitle)
        editDatails?.setText(wakaba.wakabaContent)
        contentDate?.text = wakaba.wakabaDate
        contentRg?.checkedPosition(wakaba.wakabaRating)
        wakabaNo=wakaba.wakabaNo
    }
    private fun RadioGroup.checkedPosition(num:Int) {
      val id= when (num) {
            0 -> R.id.content_r0
            1 -> R.id.content_r1
            2 -> R.id.content_r2
            3 -> R.id.content_r3
            4 -> R.id.content_r4
            else -> R.id.content_r5
        }
        this.check(id)
    }
    private fun setChanges(vm:WakabaViewModel,position:Long){
        vm.update(
            Wakaba(
                wakabaNo        = position,
                wakabaTitle     = editTitle?.text.toString(),
                wakabaContent   = editDatails?.text.toString(),
                wakabaDate      = contentDate?.text.toString(),
                wakabaRating    = activity?.findViewById<RadioButton>(contentRg?.checkedRadioButtonId!!)?.text.toString().toInt(),
                timeStamp       = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
                checkFlag       = 0
            )
        )
    }
    private fun showDatePickerDialog(context:Activity) {
        contentDate?.let {
            DatePickerDialog(
                context,
                { view, y, m, d, -> it.text = "${y}/${m+1}/${d}" },
                LocalDateTime.now().year,
                LocalDateTime.now().monthValue - 1,
                LocalDateTime.now().dayOfMonth
            ).show()
        }
    }
}