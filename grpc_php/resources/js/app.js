import './bootstrap';

document.querySelectorAll('.player-card').forEach(card => {
    card.addEventListener('click', async function() {
        const playerName = this.dataset.player;
        const details = this.querySelector('.player-details');

        details.classList.togle('hidden');

        if (!details.dataset.loaded) {
            const response = await fetch(`/analytics?player=${encodeURIComponent(playerName)}`);
            details.innerHTML = await response.text();
            details.dataset.load = 'true';
        }
    })
})