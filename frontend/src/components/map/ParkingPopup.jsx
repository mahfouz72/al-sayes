export const createPopupContent = (lot, onSelect) => `
  <div class="text-center">
    <h3 class="font-semibold">${lot.name}</h3>
    <p class="text-sm">${lot.location}</p>
    <p class="text-sm">Available: ${lot.availableSpots}/${lot.totalSpots}</p>
    <button 
      class="mt-2 px-3 py-1 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700"
      onclick="document.dispatchEvent(new CustomEvent('selectLot', {detail: ${lot.id}}))"
    >
      Reserve
    </button>
  </div>
`;
