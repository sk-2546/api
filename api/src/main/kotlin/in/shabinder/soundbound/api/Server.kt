package `in`.shabinder.soundbound.api

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true; encodeDefaults = true })
        }
        routing {
            route("/api") {
                get("/health") {
                    call.respond(mapOf("status" to "ok"))
                }

                get("/songs") {
                    call.respond(SongRepository.getAll())
                }

                get("/songs/{id}") {
                    val id = call.parameters["id"]?.toLongOrNull()
                    if (id == null) {
                        call.respond(mapOf("error" to "invalid id"))
                        return@get
                    }
                    val song = SongRepository.get(id)
                    if (song == null) call.respond(mapOf("error" to "not found"))
                    else call.respond(song)
                }
            }
        }
    }.start(wait = true)
}

@Serializable
data class SongDto(
    val id: Long,
    val title: String,
    val durationSec: Long,
    val year: Int,
    val artists: List<String>,
    val genre: List<String>,
    val trackURL: String,
    val albumName: String? = null,
    val albumArtURL: String? = null
)

object SongRepository {
    private val songs = listOf(
        SongDto(
            id = 1,
            title = "Sample Song",
            durationSec = 210,
            year = 2023,
            artists = listOf("Artist One"),
            genre = listOf("Pop"),
            trackURL = "https://example.com/track/1",
            albumName = "Sample Album",
            albumArtURL = "https://example.com/album/1.jpg"
        ),
        SongDto(
            id = 2,
            title = "Another Song",
            durationSec = 180,
            year = 2020,
            artists = listOf("Artist Two"),
            genre = listOf("Rock"),
            trackURL = "https://example.com/track/2",
            albumName = null,
            albumArtURL = null
        )
    )

    fun getAll(): List<SongDto> = songs
    fun get(id: Long): SongDto? = songs.find { it.id == id }
}
