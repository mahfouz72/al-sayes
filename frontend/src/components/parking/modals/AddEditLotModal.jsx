import { Fragment, useState } from 'react';
import { Dialog, Transition } from '@headlessui/react';
import { XMarkIcon } from '@heroicons/react/24/outline';
import { SpotTypeSection  } from './SpotTypeSection';
import { RulesSection } from './RulesSection';
export default function AddEditLotModal({ isOpen, onClose, lot = null }) {
  const [formData, setFormData] = useState({
    name: lot?.name || '',
    location: lot?.location || '',
    spotTypes: {
      REGULAR: {
        capacity: lot?.spotTypes?.REGULAR?.capacity || 0,
        basePricePerHour: lot?.spotTypes?.REGULAR?.basePricePerHour || 0,
      },
      DISABLED: {
        capacity: lot?.spotTypes?.DISABLED?.capacity || 0,
        basePricePerHour: lot?.spotTypes?.DISABLED?.basePricePerHour || 0,
      },
      EV_CHARGING: {
        capacity: lot?.spotTypes?.EV_CHARGING?.capacity || 0,
        basePricePerHour: lot?.spotTypes?.EV_CHARGING?.basePricePerHour || 0,
      },
    },
    rules: {
      timeLimit: lot?.rules?.timeLimit || 24,
      automaticReleaseTime: lot?.rules?.automaticReleaseTime || 0.5,
      notShowingUpPenalty: lot?.rules?.notShowingUpPenalty || 10,
      overTimeScale: lot?.rules?.overTimeScale || 1.5,
    },
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission
    console.log('Form data:', formData);
    onClose();
  };

  const updateSpotType = (type, field, value) => {
    setFormData({
      ...formData,
      spotTypes: {
        ...formData.spotTypes,
        [type]: {
          ...formData.spotTypes[type],
          [field]: value,
        },
      },
    });
  };

  const updateRule = (rule, value) => {
    setFormData({
      ...formData,
      rules: {
        ...formData.rules,
        [rule]: value,
      },
    });
  };

  return (
    <Transition.Root show={isOpen} as={Fragment}>
      <Dialog as="div" className="relative z-10" onClose={onClose}>
        <Transition.Child
          as={Fragment}
          enter="ease-out duration-300"
          enterFrom="opacity-0"
          enterTo="opacity-100"
          leave="ease-in duration-200"
          leaveFrom="opacity-100"
          leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" />
        </Transition.Child>

        <div className="fixed inset-0 z-10 overflow-y-auto">
          <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
            <Transition.Child
              as={Fragment}
              enter="ease-out duration-300"
              enterFrom="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
              enterTo="opacity-100 translate-y-0 sm:scale-100"
              leave="ease-in duration-200"
              leaveFrom="opacity-100 translate-y-0 sm:scale-100"
              leaveTo="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
            >
              <Dialog.Panel className="relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-2xl sm:p-6 max-h-[80vh] overflow-y-auto">
                <div className="absolute right-0 top-0 pr-4 pt-4">
                  <button
                    type="button"
                    className="rounded-md bg-white text-gray-400 hover:text-gray-500"
                    onClick={onClose}
                  >
                    <span className="sr-only">Close</span>
                    <XMarkIcon className="h-6 w-6" aria-hidden="true" />
                  </button>
                </div>
                <div className="sm:flex sm:items-start">
                  <div className="mt-3 text-center sm:mt-0 sm:text-left w-full">
                    <Dialog.Title as="h3" className="text-lg font-semibold leading-6 text-gray-900">
                      {lot ? 'Edit Parking Lot' : 'Add New Parking Lot'}
                    </Dialog.Title>
                    <form onSubmit={handleSubmit} className="mt-6 space-y-6">
                      {/* Basic Information */}
                      <div className="space-y-4">
                        <h4 className="text-md font-medium text-gray-900">Basic Information</h4>
                        <div>
                          <label htmlFor="name" className="block text-sm font-medium text-gray-700">
                            Name
                          </label>
                          <input
                            type="text"
                            id="name"
                            placeholder="Enter Parking Lot Name"
                            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                            required
                          />
                        </div>
                        <div>
                          <label htmlFor="location" className="block text-sm font-medium text-gray-700">
                            Location
                          </label>
                          <input
                            type="text"
                            id="location"
                            placeholder={formData.location}
                            onChange={(e) => setFormData({ ...formData, location: e.target.value })}
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                            required
                          />
                        </div>
                      </div>

                      <SpotTypeSection 
                        spotTypes={formData.spotTypes} 
                        onUpdate={updateSpotType} 
                      />

                      <RulesSection 
                        rules={formData.rules} 
                        onUpdate={updateRule} 
                      />

                      <div className="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
                        <button
                          type="submit"
                          className="inline-flex w-full justify-center rounded-md bg-blue-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-blue-500 sm:ml-3 sm:w-auto"
                        >
                          {lot ? 'Save Changes' : 'Add Parking Lot'}
                        </button>
                        <button
                          type="button"
                          className="mt-3 inline-flex w-full justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:mt-0 sm:w-auto"
                          onClick={onClose}
                        >
                          Cancel
                        </button>
                      </div>
                    </form>
                  </div>
                </div>
              </Dialog.Panel>
            </Transition.Child>
          </div>
        </div>
      </Dialog>
    </Transition.Root>
  );
}