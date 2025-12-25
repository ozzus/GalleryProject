package com.example.galleryproject.data.photos

import com.example.galleryproject.models.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor() {
    fun getPhotos(): List<Photo> {
        return listOf(
            Photo(
                id = 1,
                imageUrl = "https://picsum.photos/id/10/300/300",
                title = "House View",
                author = "John Doe",
                date = "23.08.2022",
                description = "A cozy house in Italy...",
                isPopular = false
            ),
            Photo(
                id = 2,
                imageUrl = "https://picsum.photos/id/20/300/300",
                title = "Lake",
                author = "Alice Smith",
                date = "12.06.2022",
                description = "Peaceful morning on the lake.",
                isPopular = false
            ),
            Photo(
                id = 3,
                imageUrl = "https://picsum.photos/id/30/300/300",
                title = "Mountain",
                author = "Bob Ross",
                date = "10.04.2022",
                description = "A calm mountain view.",
                isPopular = true
            )
        )
    }
}
