import { Fragment } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Disclosure } from "@headlessui/react";
import { Menu, X } from "lucide-react";
import Logo from "./Logo";
import NavLink from "./NavLink";
import UserMenu from "./UserMenu";
import NotificationBell from "../notification/NotificationBell";
import "./Navbar.css";

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
        <Disclosure as="nav" className="navbar">
            {({ open }) => (
                <>
                    <div className="navbar-container">
                        <div className="navbar-content">
                            <div className="navbar-left">
                                <Logo />
                                <div className="navbar-links">
                                    {navigation.map((item) => (
                                        <NavLink key={item.name} to={item.href}>
                                            {item.name}
                                        </NavLink>
                                    ))}
                                </div>
                            </div>

                            <div className="navbar-right">
                                <Link to="/notifications">
                                    <NotificationBell />
                                </Link>
                                <UserMenu
                                    username={username}
                                    onLogout={handleLogout}
                                />
                                <div className="mobile-menu">
                                    <Disclosure.Button className="menu-button">
                                        {open ? (
                                            <X
                                                className="menu-icon"
                                                aria-hidden="true"
                                            />
                                        ) : (
                                            <Menu
                                                className="menu-icon"
                                                aria-hidden="true"
                                            />
                                        )}
                                    </Disclosure.Button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <Disclosure.Panel className="mobile-panel">
                        <div className="mobile-links">
                            {navigation.map((item) => (
                                <Disclosure.Button
                                    key={item.name}
                                    as={Link}
                                    to={item.href}
                                    className="mobile-link"
                                >
                                    {item.name}
                                </Disclosure.Button>
                            ))}
                            <Disclosure.Button
                                as={Link}
                                to="/notifications"
                                className="mobile-link"
                            >
                                Notifications
                            </Disclosure.Button>
                        </div>
                    </Disclosure.Panel>
                </>
            )}
        </Disclosure>
    );
}
