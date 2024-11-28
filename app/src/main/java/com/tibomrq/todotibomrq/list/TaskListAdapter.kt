package com.tibomrq.todotibomrq.list

import android.annotation.SuppressLint
import android.system.Os.bind
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tibomrq.todotibomrq.R
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.UUID


class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    var currentList: List<Task> = emptyList()


    // on utilise `inner` ici afin d'avoir accès aux propriétés de l'adapter directement
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView : TextView = itemView.findViewById(R.id.task_title)
        val textView2 : TextView = itemView.findViewById(R.id.task_descrp)



        fun bind(task: Task) {
            // on affichera les données ici
            textView.text = task.title
            textView2.text = task.description
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return currentList.count()
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])

    }

    public fun refreshAdapter() {
            // Instanciation d'un objet task avec des données préremplies:
            val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${currentList.size + 1}")
            currentList = currentList + newTask

    }
}