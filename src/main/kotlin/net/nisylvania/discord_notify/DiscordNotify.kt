package net.nisylvania.discord_notify

import org.bukkit.Bukkit
import net.dv8tion.jda.api.JDABuilder
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy

class DiscordNotify : JavaPlugin(), Listener {

    private lateinit var jda: net.dv8tion.jda.api.JDA
    private var notifyUserId: String = ""

    override fun onEnable() {
        // Plugin startup logic
        logger.info("Discord通知プラグインが起動しました。")

        // config.ymlの読み込み
        saveDefaultConfig()
        val token = config.getString("bot-token") ?: error("config.yml内にDiscord Botのトークンが正しく書かれていません。")
        notifyUserId = config.getString("notify-user-id") ?: error("config.yml内にDiscord Botの送信先IDが正しく書かれていません。")

        // JDAの初期化
        val intents = listOf(
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.DIRECT_MESSAGES
        )

        jda = JDABuilder.createDefault(token)
            .enableIntents(intents)  // インテントを有効化
            .setMemberCachePolicy(MemberCachePolicy.ALL) // 全てのメンバーをキャッシュ
            .build()
        jda.awaitReady()

        Bukkit.getPluginManager().registerEvents(PlayerListeners(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
        jda.shutdown()
    }

    // 通知処理を共通化
    fun notifyDiscord(message: String) {
        if (!::jda.isInitialized) {
            logger.warning("JDAが初期化されていません。")
            return
        }

        val user = jda.getUserById(notifyUserId)
        if (user == null) {
            logger.severe("ユーザID：${notifyUserId}が見つかりません。")
        } else {
            user.openPrivateChannel().queue { channel ->
                channel.sendMessage(message).queue(
                    {  },
                    { error -> logger.severe("メッセージの送信に失敗しました。${error.message}") }
                )
            }
        }
    }
}
