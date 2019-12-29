package ru.javalab.socketsapp.services.favorites;

import ru.javalab.socketsapp.Dto.Dto;
import ru.javalab.socketsapp.context.Component;
import ru.javalab.socketsapp.models.Favorites;
import ru.javalab.socketsapp.repository.repository.Favorites.FavoritesRepositoryImpl;

public class AddToFavoritesImpl implements AddToFavorites, Component {
    private FavoritesRepositoryImpl favoritesRepositoryImpl;

    public AddToFavoritesImpl(){}

    public AddToFavoritesImpl(FavoritesRepositoryImpl favoritesRepositoryImpl){
        this.favoritesRepositoryImpl = favoritesRepositoryImpl;
    }

    @Override
    public String getName() {
        return "AddToFavorites";
    }

    @Override
    public Dto addToFavorites(Favorites favorites) {
        favoritesRepositoryImpl.save(favorites);
        return null;
    }
}
