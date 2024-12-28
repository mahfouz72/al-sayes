import Avatar from "./Avatar";
import ProfileBadge from "./ProfileBadge";

export default function ProfileHeader({ user, role }) {
    return (
        <div className="flex items-center space-x-6">
            <Avatar username={user?.username} />
            <div>
                <h1 className="text-2xl font-bold text-gray-900">
                    {user?.username}
                </h1>
                <ProfileBadge role={role} />
            </div>
        </div>
    );
}
