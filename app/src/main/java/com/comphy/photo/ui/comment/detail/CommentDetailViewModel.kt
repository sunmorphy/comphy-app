package com.comphy.photo.ui.comment.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.comphy.photo.data.repository.PostRepository
import com.comphy.photo.data.source.remote.response.post.comment.CommentBody
import com.comphy.photo.data.source.remote.response.post.comment.SecondChildComment
import com.comphy.photo.data.source.remote.response.post.comment.ThirdChildComment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CommentDetailViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    val isFetching = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val secondLevelCommentResponse = MutableLiveData<PagingData<SecondChildComment>>()
    val thirdLevelCommentResponse = MutableLiveData<List<ThirdChildComment>>()
    val successResponse = MutableLiveData<String>()

    fun getSecondLevelCommentPost(
        postId: String,
        parentId: Int
    ) = postRepository.getSecondLevelCommentPost(
        postId = postId,
        parentId = parentId
    ).cachedIn(viewModelScope)

    suspend fun getThirdLevelCommentPost(
        postId: String,
        parentId: Int
    ) = postRepository.getThirdLevelCommentPost(
        postId = postId,
        parentId = parentId
    )
        .onStart { isFetching.postValue(true) }
        .onCompletion { isFetching.postValue(false) }
        .collect { thirdLevelCommentResponse.postValue(it) }

    suspend fun commentPost(
        comment: String,
        parentId: Int? = null,
        postId: String
    ) = postRepository.commentPost(
        CommentBody(
            comment = comment,
            parentId = parentId,
            postId = postId
        )
    )
        .onStart { isLoading.postValue(true) }
        .onCompletion { isLoading.postValue(false) }
        .collect { successResponse.postValue(it.message) }

}