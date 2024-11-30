package net.nisylvania.discord_notify

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerLoginEvent.Result

class PlayerListeners(private val main: DiscordNotify) : Listener {


    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        // プレイヤーの入室イベント
        val player = event.player
        val location = player.location
        val worldName = location.world?.name ?: "不明なワールド"
        val x = String.format("%.1f",location.x)
        val y = String.format("%.1f",location.y)
        val z = String.format("%.1f",location.z)

        // 参加成功時メッセージ
        val message = "${player.name}が参加しました。\n座標：[${x}, ${y}, ${z}]（${worldName}）"
        Bukkit.getLogger().info(message)
        main.notifyDiscord(message) // Discord通知
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // プレイヤーの退出イベント
        val player = event.player
        val location = player.location
        val worldName = location.world?.name ?: "不明なワールド"
        val x = String.format("%.1f",location.x)
        val y = String.format("%.1f",location.y)
        val z = String.format("%.1f",location.z)

        // 参加成功時メッセージ
        val message = "${player.name}が退出しました。\n座標：[${x}, ${y}, ${z}]（${worldName}）"
        Bukkit.getLogger().info(message)
        main.notifyDiscord(message) // Discord通知
    }

    @EventHandler
    fun onPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
        if (event.loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            val playerName = event.name
            val reason = event.kickMessage

            // ログイン失敗時メッセージ
            val message = "${playerName}が${reason}のためログインに失敗しました。"
            Bukkit.getLogger().info(message)
            main.notifyDiscord(message) // Discord通知

        }
    }

    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent) {
        if (event.result != Result.ALLOWED) {
            val playerName = event.player.name
            val reason = event.kickMessage

            // ログイン拒否時メッセージ
            val message = "${playerName}が${reason}のためログインに失敗しました。"
            Bukkit.getLogger().info(message)
            main.notifyDiscord(message) // Discord通知

        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        // プレイヤーの死亡イベント
        val player = event.entity
        val location = player.location
        val worldName = location.world?.name ?: "不明なワールド"
        val x = String.format("%.1f",location.x)
        val y = String.format("%.1f",location.y)
        val z = String.format("%.1f",location.z)
        val deathCause = player.lastDamageCause?.cause?.name ?: "不明な死因"

        // 死亡時メッセージ
        val message = "${player.name}が死亡しました。\n死因：${deathCause}\n座標：[${x}, ${y}, ${z}]（${worldName}）"
        Bukkit.getLogger().info(message)
        main.notifyDiscord(message) // Discord通知
    }
}