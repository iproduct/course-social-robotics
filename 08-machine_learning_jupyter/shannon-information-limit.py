import numpy as np


def calculate_shannon_capacity():
    # Typical parameters for a high-performance fiber optic channel
    bandwidth = 50e9  # 50 GHz (typical channel spacing)
    snr_db = 20  # 20 dB signal-to-noise ratio (standard for coherent systems)

    # Convert SNR from decibels to linear ratio
    # SNR_linear = 10^(SNR_db / 10)
    snr_linear = 10 ** (snr_db / 10)

    # Shannon-Hartley Formula: C = B * log2(1 + S/N)
    capacity_bps = bandwidth * np.log2(1 + snr_linear)

    # Convert to Gigabits per second
    capacity_gbps = capacity_bps / 1e9

    print(f"Bandwidth: {bandwidth / 1e9} GHz")
    print(f"SNR: {snr_db} dB")
    print(f"Maximum Theoretical Capacity: {capacity_gbps:.2f} Gbps")


calculate_shannon_capacity()