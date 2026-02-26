type ErrorPageProps = {
  code?: '404' | '500';
  title?: string;
  description: string;
  actionLabel?: string;
  onAction?: () => void;
};

const ErrorPage = ({ code, title, description, actionLabel, onAction }: ErrorPageProps) => {
  const defaultTitle = code === '404' ? 'Page not found' : code === '500' ? 'Server error' : '';

  return (
    <main className="error-page" role="alert" aria-live="assertive">
      <section className="error-card">
        {code ? <p className="error-code">{code}</p> : null}
        <h1>{title ?? defaultTitle}</h1>
        <p>{description}</p>
        {actionLabel && onAction ? (
          <button type="button" className="error-action" onClick={onAction}>
            {actionLabel}
          </button>
        ) : null}
      </section>
    </main>
  );
};

export default ErrorPage;
