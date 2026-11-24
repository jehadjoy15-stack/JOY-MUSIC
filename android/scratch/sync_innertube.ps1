$files = git ls-tree -r --name-only upstream/main:innertube/src/main/kotlin/com/joymusic/innertube
Remove-Item -Recurse -Force "innertube\src\main\kotlin\com\joymusic\innertube\*" -ErrorAction SilentlyContinue

foreach ($f in $files) {
    $content = git show ("upstream/main:innertube/src/main/kotlin/com/joymusic/innertube/" + $f)
    $converted = $content -replace "com\.joymusic\.innertube", "com.joymusic.innertube"
    $targetPath = Join-Path "innertube\src\main\kotlin\com\joymusic\innertube" $f
    $targetDir = Split-Path -Parent $targetPath
    if (-not (Test-Path $targetDir)) {
        New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
    }
    [System.IO.File]::WriteAllText($targetPath, ($converted -join "`n"), [System.Text.Encoding]::UTF8)
}
Write-Output "Innertube sync complete"
