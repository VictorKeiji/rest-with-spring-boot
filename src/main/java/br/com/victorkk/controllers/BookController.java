package br.com.victorkk.controllers;

import br.com.victorkk.data.vo.v1.BookVO;
import br.com.victorkk.services.BookServices;
import br.com.victorkk.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Books", description = "Endpoints for Managing Books")
public class BookController {

    @Autowired
    private BookServices service;

    //@CrossOrigin(origins = "http://localhost:8080") >>> Cross Origin declarado globalmente em config/webconfig
    @GetMapping(
        produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds all Books",
            description = "Shows all books in a JSON, XML or YML representation",
            tags = {"Books"},
            responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content (
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
                    )),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        })
    public List<BookVO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds a Book",
            description = "Shows a book in a JSON, XML or YML representation",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content (schema = @Schema(implementation = BookVO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public BookVO findById(@PathVariable(value="id") Long id) {
        return service.findById(id);
    }

    @PostMapping(
        consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
        produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adds a new Book",
            description = "Adds a new book by passing in a JSON. XML or YML representation",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content (schema = @Schema(implementation = BookVO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public BookVO create(@RequestBody BookVO book) {
        return service.create(book);
    }

    @PutMapping(
        consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
        produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Updates a Book",
            description = "Updates a book by passing in a JSON. XML or YML representation",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content (schema = @Schema(implementation = BookVO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public BookVO update(@RequestBody BookVO book) {
        return service.update(book);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes Book",
            description = "Deletes a book by passing in a JSON. XML or YML representation",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
