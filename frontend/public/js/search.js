// US04:

const searchInput = document.getElementById('search-input');
let searchTimer = null;

searchInput.addEventListener('input', () => {
    clearTimeout(searchTimer);
  
        appState.keyword = searchInput.value.trim();
        loadLots();
    }, 300);
});

document.getElementById('search-clear').addEventListener('click', () => {
    searchInput.value = '';
    appState.keyword = '';
    loadLots();
    map.setView(MAP_CENTER, MAP_ZOOM);
});