package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MatchResponseDto(
    @SerializedName("matches") val matches: List<MatchDto>
)

data class MatchDto(
    @SerializedName("id_match") val idMatch: Int,
    @SerializedName("tournament_id") val tournamentId: String?,
    @SerializedName("phase") val phase: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("time") val time: String?,
    @SerializedName("timezone") val timezone: String?,
    @SerializedName("stadium_id") val stadiumId: Int,
    @SerializedName("home_team_id") val homeTeamId: Int,
    @SerializedName("away_team_id") val awayTeamId: Int
)

data class TeamResponseDto(
    @SerializedName("teams") val teams: List<TeamDto>
)

data class TeamDto(
    @SerializedName("id_team") val idTeam: Int,
    @SerializedName("name") val name: String,
    @SerializedName("group") val group: String?,
    @SerializedName("code") val code: String
)

data class StadiumResponseDto(
    @SerializedName("stadiums") val stadiums: List<StadiumDto>
)

data class StadiumDto(
    @SerializedName("id_stadium") val idStadium: Int,
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("stadiumName") val stadiumName: String,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("displayVenueName") val displayVenueName: String?
)

data class TournamentResponseDto(
    @SerializedName("tournament") val tournament: TournamentDto
)

data class TournamentDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("totalTeams") val totalTeams: Int,
    @SerializedName("totalMatches") val totalMatches: Int
)
