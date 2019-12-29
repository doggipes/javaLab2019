package ru.javalab.socketsapp.repository.repository.Favorites;

import ru.javalab.socketsapp.models.Favorites;
import ru.javalab.socketsapp.repository.repository.CrudRepository;

public interface FavoritesRepository extends CrudRepository<Favorites, Integer> {
    void save(Favorites favorites);
}
