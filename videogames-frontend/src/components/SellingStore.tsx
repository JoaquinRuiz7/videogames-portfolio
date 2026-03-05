type SellingStoreProps = {
  logo: string;
  name: string;
};
const SellingStore = ({ logo, name }: SellingStoreProps) => {
  return (
    <div className="deal-store-info">
      <img src={logo} alt={`${name} logo`} className="deal-store-logo" />
      <p>
        Store: <strong>{name}</strong>
      </p>
    </div>
  );
};

export default SellingStore;
