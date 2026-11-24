{ lib, stdenv, appimageTools, fetchurl, release }:
let
  pname = "joymusic";
  version = release.version;
  asset = release.linux.${stdenv.hostPlatform.system};
  src = fetchurl { inherit (asset) url hash; };
  contents = appimageTools.extract { inherit pname version src; };
in
appimageTools.wrapType2 {
  inherit pname version src;
  extraPkgs = pkgs: with pkgs; [
    gtk3 webkitgtk_4_1 libsecret dbus alsa-plugins
  ];
  extraInstallCommands = ''
    install -Dm644 ${contents}/JoyMusic.desktop $out/share/applications/joymusic.desktop
    substituteInPlace $out/share/applications/joymusic.desktop \
      --replace-fail 'Exec=bin/JoyMusic' 'Exec=joymusic'
    install -Dm644 ${contents}/JoyMusic.png $out/share/icons/hicolor/512x512/apps/JoyMusic.png
  '';
  meta = {
    description = "YouTube Music desktop client";
    homepage = "https://github.com/jehadjoy15-stack/JOY-MUSIC";
    license = lib.licenses.gpl3;
    platforms = [ "x86_64-linux" "aarch64-linux" ];
    mainProgram = pname;
  };
}
