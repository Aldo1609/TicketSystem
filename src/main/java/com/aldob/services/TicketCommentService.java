package com.aldob.services;

import java.util.List;

import com.aldob.dtos.requests.AddCommentRequest;
import com.aldob.dtos.responses.AddCommentResponse;
import com.aldob.dtos.responses.GetCommentsResponse;

public interface TicketCommentService {

    AddCommentResponse addComment(Long ticketId,AddCommentRequest request);

    List<GetCommentsResponse> getComments(Long ticketId);

}
