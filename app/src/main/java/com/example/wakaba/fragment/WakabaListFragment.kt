package com.example.wakaba.fragment

import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.example.wakaba.R
import com.example.wakaba.model.SnackMsg
import com.example.wakaba.model.WakabaRecycleAdapter
import com.example.wakaba.model.WakabaViewModel
import com.example.wakaba.room.Wakaba
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WakabaListFragment : Fragment() {
    private val viewMode: WakabaViewModel by    lazy{
        ViewModelProvider(this).get(WakabaViewModel::class.java)
    }
    private val myAdapter:WakabaRecycleAdapter by lazy {
        WakabaRecycleAdapter()
    }
    private var viewGroup: ViewGroup? = null
    private var recyclerView: RecyclerView? = null
    private var sendButton:FloatingActionButton?=null
    private var check: ImageView?=null
    private var safeArg:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arg->safeArg=WakabaListFragmentArgs.fromBundle(arg).check }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {getContext->
            recycleInit(getContext,viewMode)
        }

        viewMode?.let {viewMode->
            viewMode.getAllData().observe(viewLifecycleOwner, Observer {bean->
                myAdapter?.let{ad->ad.submitList(bean)}
            })
            viewGroup?.findViewById<FloatingActionButton>(R.id.fabSendForm)?.setOnClickListener {view->
                activity?.let { showDialog(it) }
            }
        }
        val itemTouchHelper= myAdapter?.let { getSwipeHelper(it) }

        itemTouchHelper?.attachToRecyclerView(recyclerView)
        safeArg?.let {
            when(it){
                1->{
                    Snackbar.make(viewGroup!!,SnackMsg.UPDATE_MSG.get,Snackbar.LENGTH_SHORT)
                        .setTextColor(Color.GREEN)
                        .show()
                    safeArg=0
                    }
                else->return@let
        }}
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        container?.let {vg->
            viewGroup = vg
            sendButton=vg.findViewById(R.id.fabSendForm)
            check=vg.findViewById(R.id.check_task)
        }
        return inflater.inflate(R.layout.fragment_wakaba_list, container, false)
    }

    private fun showDialog(context: Context){
        val timeNow= LocalDateTime.now()
        val layoutView=layoutInflater.inflate(R.layout.dialog_form,null,false)as LinearLayout
        var rating:Int=0
        val wakabaDate=layoutView.findViewById<TextView>(R.id.text_date).apply {
            text=timeNow.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        }
        val wakabaTitle=layoutView.findViewById<TextInputEditText>(R.id.edit_title)
        val wakabaContent=layoutView.findViewById<TextInputEditText>(R.id.edit_content)

        layoutView.findViewById<RadioGroup>(R.id.radio_group_rating).run{
            setOnCheckedChangeListener { radioGroup, i ->
                val radioButton=radioGroup.findViewById<RadioButton>(i)
                rating=radioButton.text.toString().toInt()
            }
        }
        layoutView.findViewById<ImageView>(R.id.btn_input_date).setOnClickListener {
                   DatePickerDialog(
                       context,
                       { view, y, m, d -> wakabaDate.text="${y}/${m+1}/${d}" },
                       timeNow.year,
                       timeNow.monthValue-1,
                       timeNow.dayOfMonth).show()
        }
        val dialog= AlertDialog.Builder(context)
            .setView(layoutView)
            .create()
        dialog.show()
        layoutView.findViewById<Button>(R.id.btn_save).setOnClickListener {
            viewMode?.let {vm->
                vm.insert(
                    Wakaba(
                        wakabaNo        =   0,
                        wakabaTitle     =   wakabaTitle.text.toString(),
                        wakabaContent   =   wakabaContent.text.toString(),
                        wakabaRating    =   rating,
                        wakabaDate      =   wakabaDate.text.toString(),
                        timeStamp       =   timeNow.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
                        checkFlag       =   0
                    )
                )
            }
            dialog.dismiss()
            viewGroup?.let { vg ->
                Snackbar.make(vg,SnackMsg.INSERT_MSG.get,Snackbar.LENGTH_SHORT)
                    .setTextColor(Color.CYAN)
                    .show()
            }
        }
    }

    private fun getSwipeHelper(adapter:WakabaRecycleAdapter) =
        ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean { TODO("Not yet implemented") }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
              viewGroup?.let { vg ->
                  if (direction == ItemTouchHelper.LEFT) {
                      when (adapter.getItemAt(viewHolder.adapterPosition).checkFlag) {
                          0 -> {
                              viewMode.update(
                                    adapter.getItemAt(viewHolder.adapterPosition).apply { checkFlag = 1 })
                                    adapter.run { notifyItemChanged(viewHolder.adapterPosition) }
                                    Snackbar.make(vg, SnackMsg.COMPLETE_MSG.get, Snackbar.LENGTH_SHORT)
                                        .setTextColor(Color.GREEN)
                                        .show()
                          }
                          else -> {
                              viewMode.delete(adapter.getItemAt(viewHolder.adapterPosition))
                              Snackbar.make(vg, SnackMsg.DELETE_MSG.get, Snackbar.LENGTH_SHORT)
                                  .setTextColor(Color.rgb(247, 192, 192))
                                  .show()
                            }
                        }
                  }else if(direction==ItemTouchHelper.RIGHT){
                      when(adapter.getItemAt(viewHolder.adapterPosition).checkFlag){
                          1 -> {
                              adapter.run {
                                  viewMode.update(getItemAt(viewHolder.adapterPosition).apply {
                                      checkFlag = 0
                                  })
                                  notifyItemChanged(viewHolder.adapterPosition)
                              }
                              Snackbar.make(vg, SnackMsg.RETURN_TASK.get, Snackbar.LENGTH_SHORT)
                                  .setTextColor(Color.YELLOW)
                                  .show()
                            }
                        }
                      adapter.notifyDataSetChanged()
                    }
                }
            }
        })

    private fun recycleInit(activity: Activity,viewModel: WakabaViewModel){
        val layout=LinearLayoutManager(activity).apply { orientation=LinearLayoutManager.VERTICAL }
        recyclerView    =   activity.findViewById<RecyclerView>(R.id.wakaba_recycler).also {
            it.adapter=myAdapter
            it.layoutManager=layout
            it.addItemDecoration(DividerItemDecoration(it.context,layout.orientation))
        }
    }
    private fun snackbarAiction(vg: ViewGroup,select:Int){
        val msg=""
        when(select){
            0   ->  SnackMsg.INSERT_MSG.get
            1   ->  SnackMsg.DELETE_MSG.get
            2   ->  SnackMsg.COMPLETE_MSG.get
            3   ->  SnackMsg.UPDATE_MSG.get
        }
        //Snackbar.make(vg,)
    }
}