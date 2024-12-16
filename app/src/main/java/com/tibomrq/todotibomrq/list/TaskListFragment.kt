package com.tibomrq.todotibomrq.list

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tibomrq.todotibomrq.R
import com.tibomrq.todotibomrq.data.Api
import com.tibomrq.todotibomrq.data.TaskListViewModel
import com.tibomrq.todotibomrq.detail.DetailActivity
import kotlinx.coroutines.launch
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    //private val adapter = TaskListAdapter()
    private val viewModel: TaskListViewModel by viewModels()


    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
            val userTextView = view?.findViewById<TextView>(R.id.textView2)
            userTextView?.text = user.name
        }
        val imageView = view?.findViewById<ImageView>(R.id.imageView2)
        viewModel.refresh()
        imageView?.load("https://goo.gl/gEgYUd")
    }



    val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            viewModel.delete(task.id)
        }
        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Task.TASK_KEY, task)
            editTask.launch(intent)}
    }
    val adapter = TaskListAdapter(adapterListener)



    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra(Task.TASK_KEY) as Task?
        if (task != null) {

            viewModel.create(task)
        }

    }

    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra(Task.TASK_KEY) as Task?
        if (task != null) {

            viewModel.update(task)
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_tasklist)
        recyclerView.adapter = adapter
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
            .setOnClickListener() {
                val intent = Intent(context, DetailActivity::class.java)

                createTask.launch(intent)

            }
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est exécutée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter
                adapter.currentList = newList
                adapter.notifyDataSetChanged()
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)

       // adapter.currentList = taskList

        rootView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
            .setOnClickListener() {
                val intent = Intent(context, DetailActivity::class.java)
                startActivity(intent)
                createTask.launch(intent)

            //adapter.refreshAdapter()
            }
     /*   adapter.onClickDelete = { task ->
            var newTaskList  : List<Task> = listOf()
            for (i in adapter.currentList){
                if (i.id != task.id){
                    newTaskList += i
                }
            }
            adapter.currentList = newTaskList
            adapter.notifyDataSetChanged()
        }*/

    /*    adapter.onClickEdit = { task ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Task.TASK_KEY, task)
            editTask.launch(intent)

        }*/

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}





