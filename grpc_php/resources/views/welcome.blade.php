<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Spelers Analytics</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
            min-height: 100vh;
            padding: 40px 20px;
            color: #fff;
        }
        h1 {
            text-align: center;
            margin-bottom: 30px;
            font-size: 2rem;
            color: #00d4ff;
        }
        .player-container {
            max-width: 600px;
            margin: 0 auto 15px;
        }
        .player-card {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 12px;
            overflow: hidden;
            cursor: pointer;
            transition: all 0.3s ease;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
        .player-card:hover {
            background: rgba(255, 255, 255, 0.15);
            transform: translateY(-2px);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
        }
        .player-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px 25px;
            font-size: 1.1rem;
            font-weight: 500;
        }
        .arrow {
            transition: transform 0.3s ease;
            color: #00d4ff;
        }
        .player-card.open .arrow {
            transform: rotate(180deg);
        }
        .player-details {
            padding: 0 25px;
            max-height: 0;
            overflow: hidden;
            transition: all 0.3s ease;
            background: rgba(0, 0, 0, 0.2);
        }
        .player-details.show {
            padding: 20px 25px;
            max-height: 300px;
        }
        .player-details p {
            margin: 8px 0;
            color: #ccc;
        }
        .player-details .label {
            color: #00d4ff;
            font-weight: 600;
        }
        .loading {
            color: #888;
            font-style: italic;
        }
        .error {
            color: #ff6b6b;
        }
    </style>
</head>
<body>
    <h1>⚽ Spelers Analytics</h1>
    
    <div class="player-container">
        <div class="player-card" data-player="Bryan Heynen">
            <div class="player-header">
                <span>Bryan Heynen</span>
                <span class="arrow">▼</span>
            </div>
            <div class="player-details">
            </div>
        </div>
    </div>
    
    <div class="player-container">
        <div class="player-card" data-player="Konstaninos Karetsas">
            <div class="player-header">
                <span>Konstaninos Karetsas</span>
                <span class="arrow">▼</span>
            </div>
            <div class="player-details">
            </div>
        </div>
    </div>
    
    <div class="player-container">
        <div class="player-card" data-player="Matte Smets">
            <div class="player-header">
                <span>Matte Smets</span>
                <span class="arrow">▼</span>
            </div>
            <div class="player-details">
            </div>
        </div>
    </div>

    <script>
        document.querySelectorAll('.player-card').forEach(card => {
            card.addEventListener('click', async function() {
                const playerName = this.dataset.player;
                const details = this.querySelector('.player-details');
                
                // Toggle open/close state
                this.classList.toggle('open');
                details.classList.toggle('show');
                
                // Only fetch if not already loaded
                if (!details.dataset.loaded) {
                    details.innerHTML = '<p class="loading">Laden...</p>';
                    
                    try {
                        const response = await fetch(`/analytics?player=${encodeURIComponent(playerName)}`);
                        if (response.ok) {
                            details.innerHTML = await response.text();
                            details.dataset.loaded = 'true';
                        } else {
                            details.innerHTML = '<p class="error">Fout bij ophalen data</p>';
                        }
                    } catch (error) {
                        details.innerHTML = '<p class="error">Fout: ' + error.message + '</p>';
                    }
                }
            });
        });
    </script>
</body>
</html>