package pl.sztukakodu.bookaro.catalog.domain;

import java.util.Collection;

public interface CatalogRepository {
    Collection<Book> listAll();
}
