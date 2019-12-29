package ru.javalab.socketsapp.services.favorites;

import ru.javalab.socketsapp.Dto.Dto;
import ru.javalab.socketsapp.models.Favorites;

public interface AddToFavorites {
    Dto addToFavorites(Favorites favorites);
}
