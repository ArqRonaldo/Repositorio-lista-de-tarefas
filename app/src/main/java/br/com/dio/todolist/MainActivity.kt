package br.com.dio.todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import br.com.dio.todolist.databinding.ActivityMainBinding
import br.com.dio.todolist.datasource.TaskDataSource
import br.com.dio.todolist.ui.AddTaskActivity
import br.com.dio.todolist.ui.TaskListAdapter

class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding
   private val adapter by lazy { TaskListAdapter() }


    private val register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK) updateList()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

         binding.rvTasks.adapter = adapter
         updateList()

         insertListeners()
          //DATA STORE
          //ROOM
    }

     private fun insertListeners() {
         binding.fab.setOnClickListener {
          //   startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
             register.launch(Intent(this,AddTaskActivity::class.java))
         }

         adapter.listenerEdit = {
             val intent = Intent(this, AddTaskActivity::class.java)
             intent.putExtra(AddTaskActivity.TASK_ID, it.id)
           //  startActivityForResult(intent, CREATE_NEW_TASK)
             register.launch(intent)
         }

         adapter.listenerDelete = {
             TaskDataSource.deleteTask(it)
             updateList()
         }
     }

  /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            // super.onActivityResult(requestCode, resultCode, data)
             if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
       }*/


       private fun updateList(){
            val list = TaskDataSource.getList()
            binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE
                else View.GONE

                adapter.submitList(list)
       }

    /*  companion object{
          private const val CREATE_NEW_TASK = 1000
      }*/


}