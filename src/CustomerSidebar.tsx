import settingsIcon from './img/settings-blue.png';
import detailsIcon from './img/user -blue.png';
import borrowsIcon from './img/book-color.png';
import logoutIcon from './img/logout.png';
import { Link } from 'react-router-dom';
import logout from './logout';

type Props = {
    setDisplayLogoutText: React.Dispatch<React.SetStateAction<boolean>>;
}

const CustomerSidebar = (props: Props) => {

    const handleChangeStyle = (e: React.MouseEvent<HTMLAnchorElement, MouseEvent>) => {
        const text = e.currentTarget;
        text.style.fontWeight = '900';

        const parent = document.querySelector(".sidebar")!;

        parent.querySelectorAll('a').forEach(child => {
            if (child !== text) {
                child.style.fontWeight='500';
            }
        })

    }

    return ( 
        <section className="sidebar">
            <section className="sidebar__element">
                <img src={borrowsIcon} alt="details icon" width="45" height="45" className='icon' />
                <Link className='link' to='/customer/borrows' onClick={(e) => {handleChangeStyle(e)}}>My Borrows</Link>
            </section>
            <section className="sidebar__element">
                <img src={detailsIcon} alt="details icon" width="45" height="45" className='icon' />
                <Link className='link' to='/customer/details' onClick={(e) => {handleChangeStyle(e)}}>Details</Link>
            </section>
            <section className="sidebar__element">
                <img src={settingsIcon} alt="settings icon" width="45" height="45" className='icon' />
                <Link className='link' to='/customer/settings' onClick={(e) => {handleChangeStyle(e)}}>Settings</Link>
            </section>
            <section className="sidebar__element">
                <img src={logoutIcon} alt="details icon" width="45" height="45" className='icon' />
                <Link className='link' to='/' onClick={() => logout(props.setDisplayLogoutText)}>Log Out</Link>
            </section>
        </section>
     );
}
 
export default CustomerSidebar;