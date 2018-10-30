package jp.nephy.vrchakt.endpoints

import jp.nephy.vrchakt.VRChaKtClient
import jp.nephy.vrchakt.core.VRChaKtJsonArrayAction
import jp.nephy.vrchakt.models.User

@Suppress("UNUSED")
class User(override val client: VRChaKtClient): VRChaKtEndpoint {
    fun register(username: String, password: String, email: String, birthday: String? = null, acceptedTOSVersion: String? = null) = client.session.post("/auth/register") {
        body {
            form {
                add("username" to username, "password" to password, "email" to email, "birthday" to birthday, "acceptedTOSVersion" to acceptedTOSVersion)
            }
        }
    }.empty()

    fun currentUser() = client.session.get("/auth/user").jsonObject<User.CurrentUser>()

    fun updateInfo(id: String, email: String? = null, birthday: String? = null, acceptedTOSVersion: String? = null, tags: List<String>? = null, networkSessionId: String? = null, status: User.OnlineStatus? = null, statusDescription: String? = null) = client.session.put("/users/$id") {
        parameter("email" to email, "birthday" to birthday, "acceptedTOSVersion" to acceptedTOSVersion, "tags" to tags?.joinToString(","), "networkSessionId" to networkSessionId, "status" to status?.value, "statusDescription" to statusDescription)
    }.empty()

    fun getById(id: String) = client.session.get("/users/$id").jsonObject<User.Detail>()

    fun getByName(name: String) = client.session.get("/users/$name/name").jsonObject<User.Detail>()

    fun search(filter: ListUserFilter = ListUserFilter.Any, search: String? = null, developerType: User.DeveloperType? = null, n: Int? = null, offset: Int? = null): VRChaKtJsonArrayAction<User.Simple> {
        val path = when (filter) {
            ListUserFilter.Active -> "/users/active"
            else -> "/users"
        }

        return client.session.get(path) {
            parameter(
                    "search" to search,
                    "developerType" to developerType?.value,
                    "n" to n,
                    "offset" to offset
            )
        }.jsonArray()
    }

    enum class ListUserFilter {
        Any, Active
    }
}
