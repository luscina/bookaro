package pl.sztukakodu.bookaro.catalog.web;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sztukakodu.bookaro.catalog.application.port.CatalogInitializerUseCase;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Secured({"ROLE_ADMIN"})
public class AdminController {

    private final CatalogInitializerUseCase initializer;

    @PostMapping("/initialization")
    @Transactional
    public void initialize() {
        initializer.initialize();
    }

}
