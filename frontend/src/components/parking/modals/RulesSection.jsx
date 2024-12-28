import PropTypes from "prop-types";

RulesSection.propTypes = {
    rules: PropTypes.object.isRequired,
    onUpdate: PropTypes.func.isRequired,
};

export function RulesSection({ rules, onUpdate }) {
    return (
        <div className="space-y-4">
            <h4 className="text-md font-medium text-gray-900">Rules</h4>
            <div className="grid grid-cols-2 gap-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Time Limit (Minutes)
                    </label>
                    <input
                        type="number"
                        min={15}
                        step="5"
                        value={rules.timeLimit}
                        onChange={(e) =>
                            onUpdate("timeLimit", Number(e.target.value))
                        }
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Automatic Release Time (Minutes)
                    </label>
                    <input
                        type="number"
                        min={0}
                        step="5"
                        value={rules.automaticReleaseTime}
                        onChange={(e) =>
                            onUpdate(
                                "automaticReleaseTime",
                                Number(e.target.value)
                            )
                        }
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Not Showing Up Penalty ($)
                    </label>
                    <input
                        type="number"
                        min={0}
                        step="0.01"
                        value={rules.notShowingUpPenalty}
                        onChange={(e) =>
                            onUpdate(
                                "notShowingUpPenalty",
                                Number(e.target.value)
                            )
                        }
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Over Time Scale ($/hour)
                    </label>
                    <input
                        type="number"
                        min={1}
                        step="0.01"
                        value={rules.overTimeScale}
                        onChange={(e) =>
                            onUpdate("overTimeScale", Number(e.target.value))
                        }
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                    />
                </div>
            </div>
        </div>
    );
}
