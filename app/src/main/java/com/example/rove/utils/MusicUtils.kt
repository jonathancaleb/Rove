package com.example.rove.utils

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import com.example.rove.RovePreferences
import com.example.rove.extensions.toFormattedYear
import com.example.rove.models.Album
import com.example.rove.models.Music
import com.example.rove.player.MediaPlayerHolder
import com.example.rove.ui.UIControlInterface


object MusicUtils {

    // Returns the position in list of the current played album
    // pass selected artist from artists adapter and not from current song
    // so when played artist is selected the album position will be returned
    // if selected artist differs from played artist -1 will be returned
    @JvmStatic
    fun getPlayingAlbumPosition(
        selectedArtist: String?,
        deviceAlbumsByArtist: MutableMap<String, List<Album>>?
    ) = try {
        val album = getAlbumFromList(
            selectedArtist,
            MediaPlayerHolder.getInstance().currentSong?.album,
            deviceAlbumsByArtist
        )
        album.second
    } catch (e: Exception) {
        e.printStackTrace()
        RecyclerView.NO_POSITION
    }

    @JvmStatic
    fun getAlbumSongs(
        artist: String?,
        album: String?,
        deviceAlbumsByArtist: MutableMap<String, List<Album>>?
    ) = try {
        getAlbumFromList(
            artist,
            album,
            deviceAlbumsByArtist
        ).first.music
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    @JvmStatic
    // Returns a pair of album and its position given a list of albums
    fun getAlbumFromList(
        artist: String?,
        album: String?,
        deviceAlbumsByArtist: MutableMap<String, List<Album>>?
    ): Pair<Album, Int> {
        val albums = deviceAlbumsByArtist?.get(artist)
        return try {
            val position = albums?.indexOfFirst { it.title == album }!!
            Pair(first = albums[position], second = position)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(first = albums?.first()!!, second = 0)
        }
    }

    @JvmStatic
    fun buildSortedArtistAlbums(resources: Resources, artistSongs: List<Music>?): List<Album> {

        val sortedAlbums = mutableListOf<Album>()

        artistSongs?.let {

            try {

                val groupedSongs = it.groupBy { song -> song.album }

                val iterator = groupedSongs.keys.iterator()
                while (iterator.hasNext()) {
                    val album = iterator.next()
                    val albumSongs = groupedSongs.getValue(album).toMutableList()
                    albumSongs.sortBy { song -> song.track }
                    sortedAlbums.add(
                        Album(
                            album,
                            albumSongs.first().year.toFormattedYear(resources),
                            albumSongs,
                            albumSongs.sumOf { song -> song.duration }
                        )
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            sortedAlbums.sortBy { album -> album.year }
        }

        return sortedAlbums
    }

    @JvmStatic
    fun updateMediaPlayerHolderLists(uiControlInterface: UIControlInterface, randomMusic: Music?): Music? {

        val mediaPlayerHolder = MediaPlayerHolder.getInstance()
        val currentSong = mediaPlayerHolder.currentSong

        fun selectNewSong(filter: Set<String>): Music? {
            if (musicListContains(currentSong, filter)) {
                RovePreferences.getPrefsInstance().latestPlayedSong = randomMusic
                return randomMusic
            }
            return null
        }

        RovePreferences.getPrefsInstance().filters?.let { ft ->
            RovePreferences.getPrefsInstance().favorites?.toMutableList()?.let { fav ->
                val songs = fav.filter { favFt ->
                    musicListContains(favFt, ft)
                }
                fav.removeAll(songs.toSet())
                RovePreferences.getPrefsInstance().favorites = fav
                if (fav.isEmpty()) {
                    uiControlInterface.onFavoritesUpdated(clear = true)
                }
            }
            if (mediaPlayerHolder.isQueue != null && musicListContains(currentSong, ft)) {
                with(mediaPlayerHolder.queueSongs) {
                    val songs = filter { queueSong ->
                        musicListContains(queueSong, ft)
                    }
                    removeAll(songs.toSet())
                    mediaPlayerHolder.skip(isNext = true)
                }
            } else {
                return selectNewSong(ft)
            }
        }
        return null
    }

    @JvmStatic
    fun musicListContains(song: Music?, filter: Set<String>) = filter.contains(song?.artist) || filter.contains(song?.album) || filter.contains(song?.relativePath)
}
