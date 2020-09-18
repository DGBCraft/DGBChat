# DGBChat
 
 DGBChat is a BungeeCord Plugin for DGBCraft Server
 Provide some simple message features

## Features
- The content supports plain text and raw json text
- Forward chat messages to other servers
- Send motd when the player joins the game

## Config
[Example](example/config.yml): DGBCraft's config.yml
- `open-timestamp`: Timestamp when the server was first opened, used to calculate `%day%`
- `server-alias`: List of server aliases
- `enable-global-chat`: If enabled, Chat information on the server will be forwarded to other servers
- `global-chat-format`: Chat message format of global chat. Can use %message%
- `enable-joinmotd`: If enabled, The specified information will be sent to the player when they join the server
- `joinmotd`: List of messages received when players connected

## Placeholder
- `%message%` Message sent by player. Can only be used for global chat
- `%day%` Number of days since the server was open
- `%playerName%` Player's real name
- `%displayName%` Player's display name
- `%uuid%` Player's uuid
- `%serverName%` The name of the server where the player is connected
- `%serverAlias%` The alias of the server where the player is connected

## To Do
- Global private message