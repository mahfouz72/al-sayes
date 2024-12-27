import { Fragment } from "react";
import { Menu, Transition } from "@headlessui/react";
import { Link } from "react-router-dom";
import { User, LogOut } from "lucide-react";

export default function UserMenu({ username, onLogout }) {
    return (
        <Menu as="div" className="relative ml-3">
            <Menu.Button className="flex items-center space-x-3 text-sm rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-gray-800 p-1">
                <div className="h-9 w-9 rounded-full bg-gradient-to-r from-blue-600 to-blue-700 flex items-center justify-center text-white font-semibold shadow-lg">
                    {username?.[0]?.toUpperCase() || "U"}
                </div>
                <span className="hidden md:block text-gray-300">
                    {username}
                </span>
            </Menu.Button>

            <Transition
                as={Fragment}
                enter="transition ease-out duration-200"
                enterFrom="transform opacity-0 scale-95"
                enterTo="transform opacity-100 scale-100"
                leave="transition ease-in duration-100"
                leaveFrom="transform opacity-100 scale-100"
                leaveTo="transform opacity-0 scale-95"
            >
                <Menu.Items className="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-lg bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
                    <Menu.Item>
                        {({ active }) => (
                            <Link
                                to="/profile"
                                className={`${
                                    active ? "bg-gray-50" : ""
                                } flex items-center px-4 py-2 text-sm text-gray-700 w-full`}
                            >
                                <User className="h-4 w-4 mr-2 text-blue-600" />
                                Your Profile
                            </Link>
                        )}
                    </Menu.Item>
                    <Menu.Item>
                        {({ active }) => (
                            <button
                                onClick={onLogout}
                                className={`${
                                    active ? "bg-gray-50" : ""
                                } flex items-center px-4 py-2 text-sm text-gray-700 w-full`}
                            >
                                <LogOut className="h-4 w-4 mr-2 text-blue-600" />
                                Sign out
                            </button>
                        )}
                    </Menu.Item>
                </Menu.Items>
            </Transition>
        </Menu>
    );
}
