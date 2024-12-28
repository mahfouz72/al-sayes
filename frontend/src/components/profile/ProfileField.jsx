export default function ProfileField({ icon: Icon, label, value }) {
    return (
        <div className="flex items-center space-x-4">
            <div className="flex-shrink-0">
                <Icon className="h-6 w-6 text-blue-600" />
            </div>
            <div>
                <p className="text-sm font-medium text-gray-500">{label}</p>
                <p className="mt-1 text-lg font-semibold text-gray-900">
                    {value}
                </p>
            </div>
        </div>
    );
}
