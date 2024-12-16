package com.tibomrq.todotibomrq.data

import com.tibomrq.todotibomrq.list.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskWebService {
    @GET("/rest/v2/tasks/")
    suspend fun fetchTasks(): Response<List<Task>>

    @POST("/rest/v2/tasks/")
    suspend fun create(@Body task: Task): Response<Task>

    @POST("/rest/v2/tasks/{id}")
    suspend fun update(@Body task: Task, @Path("id") id: String = task.id): Response<Task>

// Complétez avec les méthodes précédentes, la doc de l'API, et celle de Retrofit:
    @DELETE("/rest/v2/tasks/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>
}