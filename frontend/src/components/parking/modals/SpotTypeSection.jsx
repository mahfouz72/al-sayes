import PropTypes from 'prop-types';

SpotTypeSection.propTypes = {
  spotTypes: PropTypes.object.isRequired,
  onUpdate: PropTypes.func.isRequired,
};

export function SpotTypeSection({ spotTypes, onUpdate }) {
  return (
    <div className="space-y-4">
      <h4 className="text-md font-medium text-gray-900">Spot Types</h4>
      {Object.entries(spotTypes).map(([type, data]) => (
        <div key={type} className="border rounded-md p-4">
          <h5 className="text-sm font-medium text-gray-900 mb-3">
            {type.replace('_', ' ').replace(/(\w)(\w*)/g,
        function(g0,g1,g2){return g1.toUpperCase() + g2.toLowerCase();}).replace("Ev","EV")} Spots
          </h5>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Capacity
              </label>
              <input
                type="number"
                value={data.capacity}
                onChange={(e) => onUpdate(type, 'capacity', Number(e.target.value))}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                min={1}
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Base Price per Hour ($/hr)
              </label>
              <input
                type="number"
                min="0"
                step="0.01"
                value={data.basePricePerHour}
                onChange={(e) => onUpdate(type, 'basePricePerHour', Number(e.target.value))}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                required
              />
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}