import type { ErrorInfo, ReactNode } from 'react';
import { Component } from 'react';
import ErrorPage from './ErrorPage';

type ErrorBoundaryProps = {
  children: ReactNode;
};

type ErrorBoundaryState = {
  hasError: boolean;
};

class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  public state: ErrorBoundaryState = {
    hasError: false,
  };

  public static getDerivedStateFromError(): ErrorBoundaryState {
    return { hasError: true };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo): void {
    console.error('Frontend render error:', error, errorInfo);
  }

  public render() {
    if (this.state.hasError) {
      return (
        <ErrorPage
          title="Something went wrong"
          description="Please try again. If the problem persists, contact the maintenance team."
        />
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
