import settingsIcon from './img/settings-blue.png';
import detailsIcon from './img/user -blue.png';
import borrowsIcon from './img/book-color.png';
import logoutIcon from './img/logout.png';
import { Link } from 'react-router-dom';
import logout from './logout';

type Props = {
    setSessionIsActive: React.Dispatch<React.SetStateAction<boolean>>
}

const CustomerSidebar = (props: Props) => {

    return ( 
        <section className="sidebar">
            <section className="sidebar__element">
                <img src={borrowsIcon} alt="details icon" width="45" height="45" className='icon' />
                <p><Link to='/customer/borrows'>My Borrows</Link></p>
            </section>
            <section className="sidebar__element">
                <img src={detailsIcon} alt="details icon" width="45" height="45" className='icon' />
                <p><Link to='/customer/details'>Details</Link></p>
            </section>
            <section className="sidebar__element">
                <img src={settingsIcon} alt="settings icon" width="45" height="45" className='icon' />
                <p><Link to='/customer/settings'>Settings</Link></p>
            </section>
            <section className="sidebar__element">
                <img src={logoutIcon} alt="details icon" width="45" height="45" className='icon' />
                <p><Link to='/' onClick={() => logout(props.setSessionIsActive)}>Log Out</Link></p>
            </section>
        </section>
     );
}
 
export default CustomerSidebar;