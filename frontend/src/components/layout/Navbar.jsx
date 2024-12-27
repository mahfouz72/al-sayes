import { Fragment } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Disclosure, Transition } from "@headlessui/react";
import { Menu, X } from "lucide-react";
import Logo from "./Logo";
import NavLink from "./NavLink";
import UserMenu from "./UserMenu";

export default function Navbar() {
    const username = localStorage.getItem("username");
    const role = localStorage.getItem("role");
    const navigate = useNavigate();

    const navigation = [
        { name: "Dashboard", href: "/dashboard" },
        { name: "Reservations", href: "/reservations" },
        role === "manager" && { name: "Manage Lots", href: "/manage-lots" },
        role === "admin" && { name: "System Reports", href: "/reports" },
    ].filter(Boolean);

    const handleLogout = () => {
        localStorage.clear();
        navigate("/login");
    };

    return (
        <Disclosure as="nav" className="bg-gray-800 shadow-lg">
            {({ open }) => (
                <>
                    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                        <div className="flex h-16 items-center justify-between">
                            <div className="flex items-center">
                                <Logo />
                                <div className="hidden md:ml-8 md:flex md:space-x-4">
                                    {navigation.map((item) => (
                                        <NavLink key={item.name} to={item.href}>
                                            {item.name}
                                        </NavLink>
                                    ))}
                                </div>
                            </div>

                            <div className="flex items-center">
                                <UserMenu
                                    username={username}
                                    onLogout={handleLogout}
                                />
                                <div className="md:hidden ml-4">
                                    <Disclosure.Button className="inline-flex items-center justify-center p-2 rounded-lg text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white">
                                        {open ? (
                                            <X
                                                className="h-6 w-6"
                                                aria-hidden="true"
                                            />
                                        ) : (
                                            <Menu
                                                className="h-6 w-6"
                                                aria-hidden="true"
                                            />
                                        )}
                                    </Disclosure.Button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <Disclosure.Panel className="md:hidden">
                        <div className="px-2 pt-2 pb-3 space-y-1">
                            {navigation.map((item) => (
                                <Disclosure.Button
                                    key={item.name}
                                    as={Link}
                                    to={item.href}
                                    className="block px-3 py-2 rounded-lg text-base font-medium text-gray-300 hover:text-white hover:bg-gray-700"
                                >
                                    {item.name}
                                </Disclosure.Button>
                            ))}
                        </div>
                    </Disclosure.Panel>
                </>
            )}
        </Disclosure>
    );
}
