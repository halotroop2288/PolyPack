# PolyPack

Automatically build, host and require your server resource pack!

Made for use with [Polymer](https://github.com/Patbox/polymer) plugins.
Flexible enough for vanilla!

## Config `(config/polypack_host.json)`

* `hostIp`: The IP address to host the resource pack on. Leave as `null` to use the server's IP
    * Default: `null`
* `hostPort`: The port to host the resource pack on. Make sure this port is also forwarded, so it can be accessed externally.
    * Default: `8001`
* `threadCount`: The number of threads the HTTP server is allowed to use. If the server cannot keep up with the requests, this may need to be increased.
    * Default: `3`
* `randomiseUrl`: If this is set to true, it will make the client download a new version of the resource pack each time. If you have issues with players joining and being told there was a resource pack error, set this to `true`.
    * Default: `false`

## Recommended Server Properties `(server.properties)`

* Set `require-resource-pack` to `true` so the client is mostly forced to use the resource pack.
* Set `resource-pack-prompt` to something like `"Required Mod Resources""` so the player knows why the pack is required.

## Maven

The mod can be obtained by adding the repository:

```groovy
repositories {
  maven { url 'https://repo.blucobalt.dev/repository/maven-halotroop/' }
}
```

and added to the runtime environment using:

```groovy
dependencies {
  modRuntime("com.halotroop:PolyPack:${project.polypack_version}")
}

```
with `polypack_version` being equal to the latest version found on the [maven repo](https://repo.bluecobalt.dev).
