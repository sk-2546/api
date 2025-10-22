<#
Runs the API server using the Gradle wrapper in the project root, polls the /api/health endpoint,
and reports if the server responded successfully. Designed for PowerShell on Windows.

Usage (from project root):
  .\api\test\run_api_test.ps1
#>

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
Push-Location $projectRoot

Write-Host "Starting :api:run using Gradle wrapper..."

# Start Gradle in a background job so we can poll the health endpoint
$gradleCmd = "./gradlew :api:run"
$startInfo = New-Object System.Diagnostics.ProcessStartInfo
$startInfo.FileName = "powershell.exe"
$startInfo.Arguments = "-NoProfile -Command $gradleCmd"
$startInfo.RedirectStandardOutput = $true
$startInfo.RedirectStandardError = $true
$startInfo.UseShellExecute = $false
$startInfo.CreateNoWindow = $true

$process = New-Object System.Diagnostics.Process
$process.StartInfo = $startInfo
$process.Start() | Out-Null

Write-Host "Gradle started (PID=$($process.Id)). Waiting for server to respond..."

function Test-Health {
    try {
        $resp = Invoke-RestMethod -Uri http://localhost:8080/api/health -TimeoutSec 2 -ErrorAction Stop
        return $resp
    } catch {
        return $null
    }
}

$maxAttempts = 40
$attempt = 0
while ($attempt -lt $maxAttempts) {
    $attempt++
    $res = Test-Health
    if ($res -ne $null) {
        Write-Host "Server responded:" ($res | ConvertTo-Json -Compress)
        break
    }
    Start-Sleep -Seconds 1
}

if ($res -eq $null) {
    Write-Host "Server did not respond within timeout. Check Gradle logs for errors."
    # Capture some logs if possible
    $process | ForEach-Object {
        try { $_.StandardError.ReadToEnd() | Out-String | Write-Host } catch {}
        try { $_.StandardOutput.ReadToEnd() | Out-String | Write-Host } catch {}
    }
    # Attempt graceful close
    if (-not $process.HasExited) { $process.Kill() }
    Pop-Location
    exit 1
}

Write-Host "Health check succeeded. Stopping Gradle server..."
if (-not $process.HasExited) { $process.Kill() }
Pop-Location
exit 0
