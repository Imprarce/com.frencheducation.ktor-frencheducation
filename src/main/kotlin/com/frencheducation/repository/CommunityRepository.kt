package com.frencheducation.repository

import com.frencheducation.data.model.community.Community
import com.frencheducation.data.tabel.CommunityTable
import com.frencheducation.data.tabel.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CommunityRepository {

    suspend fun addCommunity(community: Community) {
        DatabaseFactory.dbQuery {
            CommunityTable.insert { communityTable ->
                communityTable[userImage] = community.userImage
                communityTable[userName] = community.userName
                communityTable[title] = community.title
                communityTable[rating] = community.rating
                communityTable[view] = community.view
                communityTable[createTime] = community.createTime
                communityTable[lastChange] = community.lastChange
                communityTable[hasProblemResolve] = community.hasProblemResolve
                communityTable[description] = community.description
            }
        }
    }

    suspend fun getAllCommunities(): List<Community> {
        return DatabaseFactory.dbQuery {
            (CommunityTable innerJoin UserTable)
                .selectAll()
                .mapNotNull { rowToCommunity(it) }
        }
    }

    suspend fun updateCommunity(community: Community, id: Int) {
        DatabaseFactory.dbQuery {
            CommunityTable.update(
                where = {
                    CommunityTable.idCommunity.eq(id)
                }
            ) { communityTable ->
                communityTable[userImage] = community.userImage
                communityTable[userName] = community.userName
                communityTable[title] = community.title
                communityTable[rating] = community.rating
                communityTable[view] = community.view
                communityTable[createTime] = community.createTime
                communityTable[lastChange] = community.lastChange
                communityTable[hasProblemResolve] = community.hasProblemResolve
                communityTable[description] = community.description
            }
        }
    }

    suspend fun deleteCommunity(id: Int) {
        DatabaseFactory.dbQuery {
            CommunityTable.deleteWhere { CommunityTable.idCommunity.eq(id) }
        }
    }

    suspend fun getCommunityById(id: Int) = DatabaseFactory.dbQuery {
        (CommunityTable innerJoin UserTable).select { CommunityTable.idCommunity.eq(id) }
            .map { rowToCommunity(it) }
            .singleOrNull()
    }

    private fun rowToCommunity(row: ResultRow?): Community? {
        if (row == null) {
            return null
        }

        return Community(
            idCommunity = row[CommunityTable.idCommunity],
            idUser = row[CommunityTable.idUser],
            userImage = row[UserTable.imageUrl],
            userName = row[UserTable.userName],
            title = row[CommunityTable.title],
            rating = row[CommunityTable.rating],
            view = row[CommunityTable.view],
            createTime = row[CommunityTable.createTime],
            lastChange = row[CommunityTable.lastChange],
            hasProblemResolve = row[CommunityTable.hasProblemResolve],
            description = row[CommunityTable.description]
        )
    }

}