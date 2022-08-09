package com.spotifys.repository;

import com.spotifys.entity.FavouriteTrack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends MongoRepository<FavouriteTrack, Long> {
}
