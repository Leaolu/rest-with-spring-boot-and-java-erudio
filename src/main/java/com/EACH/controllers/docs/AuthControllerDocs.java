package com.EACH.controllers.docs;

import org.springframework.http.ResponseEntity;

import com.EACH.Security.Util.UserDTO;
import com.EACH.Security.Util.UserUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface AuthControllerDocs {
	
	
	@Operation(summary = "Authenticates an User", description = "Authenticates an user that already exists", tags = {"User Authentication"}, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserUtil.class))),	
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
			})
	ResponseEntity<String> authenticateUser(@RequestBody UserDTO user);
	
	@Operation(summary = "Register an User", description = "Registers a new user with a name that is unique", tags = {"User Authentication"}, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserUtil.class))),	
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
			})
	ResponseEntity<String> RegisterUser(@RequestBody UserDTO user);
	
	@Operation(summary = "Deletes an User", description = "Deletes an User by it's name and password", tags = {"User Authentication"}, responses = {
			@ApiResponse(description = "Success", responseCode = "204", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserUtil.class))),	
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
	})
	ResponseEntity<?> DeleteUser(@RequestBody UserDTO user);

}
