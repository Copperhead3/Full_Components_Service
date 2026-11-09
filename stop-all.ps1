Write-Host "Deteniendo todos los microservicios Java..." -ForegroundColor Red
Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue
Write-Host "Microservicios detenidos y puertos liberados." -ForegroundColor Green