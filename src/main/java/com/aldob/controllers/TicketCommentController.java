package com.aldob.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aldob.dtos.payload.ApiResponse;
import com.aldob.dtos.requests.AddCommentRequest;
import com.aldob.dtos.responses.AddCommentResponse;
import com.aldob.dtos.responses.GetCommentsResponse;
import com.aldob.services.TicketCommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TicketCommentController {

    private final TicketCommentService ticketCommentService;

    @PostMapping("/tickets/{ticketId}/comments")
    @Tag(name = "Ticket Comment Controller", description = "Add a comment to a ticket")
    public ResponseEntity<ApiResponse<AddCommentResponse>> addComment(
                @PathVariable Long ticketId,
                @Valid @RequestBody AddCommentRequest request
        ) {

        AddCommentResponse response = ticketCommentService.addComment(ticketId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(
                true,
                "Comment created succesfully",
                response
            )
        );

    }

    @GetMapping(path = "/tickets/{ticketId}/comments")
    @Tag(name = "Ticket Comment Controller", description = "Get comments for a ticket")
    public ResponseEntity<ApiResponse<List<GetCommentsResponse>>> getComments(@PathVariable Long ticketId) {

        List<GetCommentsResponse> comments = ticketCommentService.getComments(ticketId);

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Comments retrieved successfully",
                comments
            )
        );
    }

}
