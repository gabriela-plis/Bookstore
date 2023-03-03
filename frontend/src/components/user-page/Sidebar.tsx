import addIcon from '../../images/plus.png';
import removeIcon from '../../images/delete-button.png';
import settingsIcon from '../../images/settings-blue.png';
import detailsIcon from '../../images/user -blue.png';
import borrowsIcon from '../../images/book-color.png';
import logoutIcon from '../../images/logout.png';


import { Link } from 'react-router-dom';
import logout from '../../functions/logout';
import { useState } from 'react';

type Props = {
    employee: boolean,
    setDisplayLogoutText: React.Dispatch<React.SetStateAction<boolean>>;
}

interface LinkData {
    title: string,
    to: string,
    icon: string,
    onClick?: (e: React.MouseEvent<HTMLAnchorElement, MouseEvent>) => void;
    forEmployee: boolean
}

const UserSidebar = (props: Props) => {

    const [selectedLink, setSelectedLink] = useState('/user/borrows');

    const employeeStyle = {
        gridTemplateRows: 'repeat(6, 1fr)',
        fontSize: '1.2rem'
    }

    const handleClick = (e: React.MouseEvent<HTMLAnchorElement, MouseEvent> ) => {
        setSelectedLink(e.currentTarget.pathname);
    }

    const links: LinkData[] = [
        {title: 'My Borrows', to: '/user/borrows', icon: borrowsIcon, onClick: handleClick, forEmployee: false},
        {title: 'Add Book', to: '/user/add-book', icon: addIcon, onClick: handleClick, forEmployee: true},
        {title: 'Remove Book', to: '/user/remove-book', icon: removeIcon, onClick: handleClick, forEmployee: true},
        {title: 'Details', to: '/user/details', icon: detailsIcon, onClick: handleClick, forEmployee: false},
        {title: 'Settings', to: '/user/settings', icon: settingsIcon, onClick: handleClick, forEmployee: false},
        {title: 'Log Out', to: '/', icon: logoutIcon, onClick: (e) => logout(props.setDisplayLogoutText), forEmployee: false}
    ]

    const filteredLinks: LinkData[] = links.filter(link => {
        if (!props.employee) {
            return !link.forEmployee
        }
        return link
    });
    

    return ( 
        <section className="sidebar" style={ props.employee ? employeeStyle : {} }>
            {filteredLinks.map(link => (
                <section className="sidebar__element" key={link.to}>
                    <img 
                        src={link.icon} 
                        width="45"
                        height="45"
                        className="icon"
                    />
                    <Link
                        to={link.to}
                        className={selectedLink === link.to ? 'link marked' : 'link'}
                        onClick={link.onClick}
                    >
                        {link.title}
                    </Link>
                </section>
                ))}
        </section>
      
     );
}
 
export default UserSidebar;