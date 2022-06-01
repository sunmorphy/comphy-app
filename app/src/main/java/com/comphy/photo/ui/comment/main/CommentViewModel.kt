package com.comphy.photo.ui.comment.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.source.remote.response.post.comment.CommentBody
import com.comphy.photo.data.source.remote.response.post.comment.CommentResponseContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    val isFetching = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val commentResponse = MutableLiveData<PagingData<CommentResponseContentItem>>()
    val successResponse = MutableLiveData<String>()

    fun getCommentPost(
        postId: String
    ) = postRepository.getCommentPost(postId)
        .cachedIn(viewModelScope)

    suspend fun commentPost(
        comment: String,
        postId: String
    ) = postRepository.commentPost(
        CommentBody(
            comment = comment,
            postId = postId
        )
    )
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { successResponse.postValue(it.message) }

}