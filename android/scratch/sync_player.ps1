# InnerTubeXPlayer.kt
$content = git show "upstream/main:app/src/main/kotlin/com/metrolist/music/utils/InnerTubeXPlayer.kt"
$converted = $content -replace "com\.metrolist\.music", "com.joymusic.music"
$converted = $converted -replace "com\.metrolist\.innertube", "com.joymusic.innertube"
[System.IO.File]::WriteAllText("app\src\main\kotlin\com\joymusic\music\utils\InnerTubeXPlayer.kt", ($converted -join "`n"), [System.Text.Encoding]::UTF8)

# potoken files
$potokenFiles = git ls-tree -r --name-only upstream/main:app/src/main/kotlin/com/metrolist/music/utils/potoken
foreach ($f in $potokenFiles) {
    $content = git show ("upstream/main:app/src/main/kotlin/com/metrolist/music/utils/potoken/" + $f)
    $converted = $content -replace "com\.metrolist\.music", "com.joymusic.music"
    $converted = $converted -replace "com\.metrolist\.innertube", "com.joymusic.innertube"
    $targetPath = Join-Path "app\src\main\kotlin\com\joymusic\music\utils\potoken" $f
    [System.IO.File]::WriteAllText($targetPath, ($converted -join "`n"), [System.Text.Encoding]::UTF8)
}

# Remove obsolete files
Remove-Item -Force "app\src\main\kotlin\com\joymusic\music\utils\YTPlayerUtils.kt" -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force "app\src\main\kotlin\com\joymusic\music\utils\cipher" -ErrorAction SilentlyContinue

Write-Output "Player and PoToken sync complete"
